const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const bcrypt = require('bcrypt');
const checkAuth = require('../middleware/check_auth');
const User = require('../../models/user');
const Activation = require('../../models/user_activation');
const emailSender = require('../utils/email_sender');

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
                createHashForPassword(req.body, res);
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
router.delete('/:id', checkAuth, (req, res) => {
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
router.put('/:id', checkAuth, (req, res) => {
    handleAddressAndEmailUpdate(req.params.id, req.body, res);
});

// Hilfsfunktion um einen Hash aus dem Passwort zu erstellen
function createHashForPassword(body, res) {
    bcrypt.hash(body.password, 10, (error, hash) => {
        if (error) {
            handleError(res, 500, error);
        } else {
            const user = new User({
                _id: new mongoose.Types.ObjectId(),
                username: body.username,
                email: body.email,
                password: hash,
                verified: false,
                address: {
                    city: body.address.city,
                    street: body.address.street,
                    street_number: body.address.street_number,
                    zipcode: body.address.zipcode
                }
            });
            saveUser(user, res);
        }
    });
}

// Hilfsfunktion um einen Benutzer zu speichern
function saveUser(user, res) {
    user.save()
        .then(result => {
            console.log('Creating User: ' + result);
            createActivationLink(res, user);
            res.status(201).json({
                message: 'user was created, please check your email account to verify your account.',
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

// Hilfsfunktion um den Activationlink zu erstellen
function createActivationLink(res, user) {
    bcrypt.hash(user._id.toString(), 10, (error, hash) => {
        if (error) {
            handleError(res, 500, error);
        } else {
            console.log('Hash: ' + hash);
            const activation = new Activation({
                _id: mongoose.Types.ObjectId(),
                userIdHashed: hash,
                user: user._id
            });
            activation.save()
                .then(result => {
                    if (result) {
                        console.log('Saving Activation: ' + result);
                        const activationLink = 'http://localhost:3000/login/activate/' + user._id + '/' + encodeURIComponent(hash);
                        emailSender.sendRegistrationEmail(user.email, activationLink);
                    }
                })
                .catch(error => {
                    handleError(res, 500, error);
                });
        }
    });
}

// Hilfsfunktion um zu ermitteln, was upgedatet werdne soll
function handleAddressAndEmailUpdate(id, body, res) {
    if (body.password) {
        return res.status(403).json(
            {
                message: 'to change your password, please follow the url',
                password_reset_url: 'http://localhost:3000/api/users/' + id + 'password_reset'
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
    return response.status(statusCode).json({
        error: error
    });
}

function handleUserNotFound(response) {
    return response.status(404).json({
        message: 'user was not found'
    });
}

module.exports = router;