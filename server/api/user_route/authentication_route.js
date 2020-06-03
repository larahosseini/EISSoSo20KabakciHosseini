const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const bcrypt = require('bcrypt');
const User = require('../../models/user');
const jwt = require('jsonwebtoken');

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