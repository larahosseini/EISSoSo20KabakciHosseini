const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const User = require('../../models/user');
const jwt = require('jsonwebtoken');

// POST: benutzer einloggen
router.post('/', (req, res, next) => {
    User.findOne({email: req.body.email})
        .exec()
        .then(user => {
            if (user) {
                // vergleiche password mit dem gespeicherten
                bcrypt.compare(req.body.password, user.password, (err, result) => {
                    if (err) {
                        console.log('Authentication failed: ' + err);
                        handleAuthenticationFailed(res);
                    }
                    if (result) {
                        const token = jwt.sign({
                                email: user.email, userId: user._id
                            },
                            process.env.JWT_SECRET_KEY,
                            {
                                expiresIn: "1h"
                            }
                        );
                        return res.status(200).json(
                            {
                                message: 'Authentication successful',
                                token: token
                            }
                        );
                    }
                    handleAuthenticationFailed(res);
                });
            } else {
                handleAuthenticationFailed(res);
            }
        })
        .catch(error => {
            handleError(res, 500, error);
        });
});

// GET: benutzer aktivieren
router.get('/activate/:userId/:activationLink', (req, res) => {
    // check is activationLink exist in database
    console.log('Trying to activating the user with the id: ' + req.params.userId);
    User.findOne({_id: req.params.userId})
        .exec()
        .then(user => {
            if (user) {
                activateUser(res, user);
            } else {
                console.log('User does not exists');
                return res.status(404).json(
                    {
                        message: 'the user was not found'
                    }
                );
            }
        })
        .catch(error => {
            handleError(res, 500, error);
        });
});

// Hilfsfunktion, damit ein Benutzer aktiviert wird
function activateUser(res, user) {
    user.update({$set: {verified: true}})
        .exec()
        .then(user => {
            if (user) {
                console.log('Updated user verification: ' + user);
                res.status(200).json(
                    {
                        message: 'verification was successful',
                        resource: {
                            type: 'GET',
                            url: 'http://localhost:3000/login'
                        }
                    }
                )
            }
        })
        .catch(error => {
            handleError(res, 500, error);
        });
}

// Hilfsfunktion für verschicken von error nachrichten
function handleError(response, statusCode, error) {
    console.log('Error: ' + error);
    response.status(statusCode).json({
        error: error
    });
}

// Hilfsfunktion für verschicken von nachricht über gescheiterten login versuch
function handleAuthenticationFailed(response) {
    return response.status(401).json({
        message: 'Authentication failed'
    });
}

module.exports = router;