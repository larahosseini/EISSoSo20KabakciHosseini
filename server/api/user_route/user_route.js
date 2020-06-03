const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const bcrypt = require('bcrypt');

const User = require('../../models/user');

// POST: new User
router.post('/signup', (req, res) => {
    // überprüfe ob benutzer existiert
    User.findOne({email: req.body.email})
        .exec()
        .then(result => {
            if (result) {
                handleDuplicateEntries(result, res, req.body.email);
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
                                    message: 'the user was created, please check your email account to verify your account.',
                                    createdUser: {
                                        _id: result._id,
                                        email: result.email,
                                        password: result.password,
                                        request: {
                                            type: 'GET',
                                            url: 'http:localhost:3000/api/users/' + result._id
                                        }
                                    }
                                });
                            })
                            .catch(error => {
                                handleError(res, 500, error);
                            });
                    }
                });
            }
        })
        .catch(error => {
            handleError(res, 500, error);
        });
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

// DELETE: user mit id löschen
router.delete('/:id', (req, res) => {
    User.findByIdAndDelete({_id: req.params.id})
        .exec()
        .then(result => {
            console.log('Deleting User: ' + result);
            if (result) {
                res.status(200).json({
                    message: 'user was successful deleted',
                    deletedUser: {
                        email: result.email
                    }
                });
            } else {
                handleUserNotFound(res);
            }
        })
        .catch(error => {
            handleError(res, 500, error);
        });
});

//PUT: user mit id updaten
router.put('/:id', (req, res) => {
    handleAddressAndEmailUpdate(req.params.id, req.body, res);
});

// Hilfsfunktion um zu ermitteln, was upgedatet werdne soll
function handleAddressAndEmailUpdate(id, body, res) {
    if (body.password) {
        return res.status(403).json(
            {
                message: 'to change your password, please follow the url',
                password_reset_url: 'http://localhost:3000/api/password_reset'
            }
        );
    } else if (body.verified) {
        return res.status(405).json(
            {
                message: 'operation not supported'
            }
        );
    }
    if (body.email) {
        User.findOne({email: body.email})
            .exec()
            .then(result => {
                if (result) {
                    handleDuplicateEntries(result, res, body.email);
                } else if (body.address) {
                    const updates = {
                        email: body.email,
                        address: body.address
                    };
                    handleUpdate(id, updates, res);
                } else {
                    const updates = {
                        email: body.email
                    };
                    handleUpdate(id, updates, res);
                }
            })
            .catch(error => {
                handleError(res, 500, error);
            });
    }
}

// Hilfsfunktion, dass eine nachricht verschickt, falls die email vorhanden ist
function handleDuplicateEntries(result, res, email) {
    console.log('Searching for duplicate emails...')
    console.log('Duplicated entry found: ' + result);
    return res.status(400).json({
        message: 'the user with the email (' + email + ') exists, please signup for a new Account or try to recover your old account.'
    });
}

// Hilfsfunktion, dass die Updates durchführt
function handleUpdate(id, updates, res) {
    User.findByIdAndUpdate({_id: id}, updates, {new: true})
        .select('email address')
        .exec()
        .then(user => {
            console.log('Updating user: ' + user);
            if (user) {
                res.status(200).json({
                    message: 'Updates applied',
                    updatedUser: user
                })
            } else {
                handleUserNotFound(res);
            }
        })
        .catch(error => {
            handleError(res, 500, error);
        });
}

// Hilfsfunktion, um alle User zu bekommen
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

function handleError(response, statusCode, error) {
    console.log('Error: ' + error);
    response.status(statusCode).json({
        error: error
    });
}

function handleUserNotFound(response) {
    response.status(404).json({
        message: 'user was not found'
    });
}

module.exports = router;