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

// GET: listet alle tasks auf
router.get('/', (req, res) => {
    getAllTasks(res);
});

// GET: listet ein Task mit der id
router.get('/:id', (req, res) => {
   getTaskById(res, req.params.id);
});

// Hilfsfunktion, listet ein task mit der gegebenen id
function getTaskById(res, id) {
    Task.findById({_id: id})
        .exec()
        .then(task => {
            if (task) {
                console.log('Task: ' + task);
                res.status(200).json(task);
            }else {
                handleTaskNotFound(res);
            }
        })
        .catch(error => {
            handleError(res, 500, error);
        })
}

// Hilfsfunktion, listet alle tasks auf
function getAllTasks(res) {
    Task.find()
        .exec()
        .then(tasks => {
            console.log('Tasks: ' + tasks);
            res.status(200).json(tasks);
        })
        .catch(error => {
            handleError(res, 500, error);
        });
}

// Hilfsfunktion, wenn eine task nicht gefunden werden sollte
function handleTaskNotFound(response) {
    response.status(404).json({
        message: 'Task not found'
    });
}

// Hilfsfunktion, wenn ein fehler entsteht
function handleError(response, statusCode, error) {
    console.log('Error: ' + error);
    return response.status(statusCode).json({
        error: error
    });
}

module.exports = router;