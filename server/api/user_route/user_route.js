const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const bcrypt = require('bcrypt');

const User = require('../../models/user');

// POST: new User
router.post('/signup', (req, res) => {
    // überprüfe ob benutzer existiert
    if (checkIfUserExists(res, req.body.email)) {
        console.log('User exists');
        res.status(400).json({
            message: 'the user with the email (' + req.body.email + ') exists. Please signup for a new Account or try to recover your old account.'
        });
    } else {
        // falls nein, dann hashe das password und speicher den benutzer in der datenbank
        bcrypt.hash(req.body.password, 10, (error, hash) => {
            if (error) {
                handleError(res, 500, error);
            } else {
                const user = new User({
                    _id: new mongoose.Types.ObjectId(),
                    username: req.body.username,
                    email: req.body.email,
                    password: hash,
                    verified: false,
                    address: {
                        city: req.body.address.city,
                        street: req.body.address.street,
                        street_number: req.body.address.street_number,
                        zipcode: req.body.address.zipcode
                    }
                });
                user.save()
                    .then(result => {
                        console.log('Creating User: ' + result);
                        res.status(201).json({
                            message: 'the user was created, please check your email account to verify your account.'
                        });
                    })
                    .catch(error => {
                        handleError(res, 500, error);
                    });
            }
        });
    }
});

// GET: kriege liste aller benutzer
router.get('/', (req, res) => {
    if (Object.keys(req.query).length > 0) {
        if (req.query.email) {
            getUserByEmail(res, req.query);
        } else {
            res.status(405).json({
                message: 'NOT ALLOWED: Only Search By Email is allowed'
            });
        }
    } else {
        getAllUsers(res);
    }
});

// GET: id
router.get('/:id', (req, res) => {
   User.findById(req.params.id)
       .select('_id email password verified address')
       .exec()
       .then(result => {
           console.log(result);
           res.status(200).json(result);
       })
       .catch(error => {
       handleError(res, 500, error);
   });
});

function getAllUsers(res) {
    User.find()
        .select('_id email')
        .exec()
        .then(results => {
            console.log(results);
            const response = {
                count: results.length,
                users: results.map(user => {
                    return {
                        _id: user._id,
                        email: user.email,
                        request: {
                            type: 'GET',
                            url: 'http:localhost:3000/api/users/' + user._id
                        }
                    }
                })
            }
            res.status(200).json(response);
        })
        .catch(error => {
            console.log(error);
            res.status(500).json(
                {
                    error: error
                }
            )
        });
}

// Hilfsfunktion um Benutzer mit Username zu finden
function getUserByEmail(res, emailQuery) {
    User.find(emailQuery)
        .select('_id email password verified address')
        .exec()
        .then(results => {
            console.log(results);
            const response = {
                count: results.length,
                users: results.map(user => {
                    return {
                        _id: user._id,
                        email: user.email,
                        request: {
                            type: 'GET',
                            url: 'http:localhost:3000/users/' + user._id
                        }
                    }
                })
            }
            res.status(200).json(response);
        })
        .catch(error => {
            console.log(error);
            res.status(500).json(
                {
                    error: error
                }
            )
        });
}

// hilfsfunktion um zu überprüfen ob ein benutzer mit der email existiert
function checkIfUserExists(response, email) {
    return User.findOne({email: email})
        .exec()
        .then(result => {
            console.log('Searching for duplicate emails...')
            if (result) {
                console.log('User exists: ' + result);
            }
            return result;
        })
        .catch(error => {
            handleError(response, 500, error);
        });
}

function handleError(response, statusCode, error) {
    console.log('Error: ' + error);
    response.status(statusCode).json({
        error: error
    });
}

module.exports = router;