const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const checkAuth = require('../middleware/check_auth');
const User = require('../../models/user');
const Task = require('../../models/task');
const emailSender = require('../utils/email_sender');


// POST: erstelle neue Task
router.post('/', (req, res) => {
    const task = new Task({
        _id: mongoose.Types.ObjectId(),
        creator: req.body.creator,
        title: req.body.title,
        description: req.body.description
    });
    task.save()
        .then(result => {
            console.log('Created Task:' + result);
            res.status(201).json(result);
        }).catch(error => {
        handleError(res, 500, error);
    });
});



function handleError(response, statusCode, error) {
    console.log('Error: ' + error);
    return response.status(statusCode).json({
        error: error
    });
}

module.exports = router;