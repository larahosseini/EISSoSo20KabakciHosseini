const express = require('express');
const app = express();
const morgan = require('morgan');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const userRoute = require('./api/user_route/user_route');
const authenticationRoute = require('./api/user_route/authentication_route');
const taskRoute = require('./api/task_route/task_route');

// database setup
mongoose.connect('mongodb://localhost:27017/db_helper',
    {useNewUrlParser: true, useUnifiedTopology: true});

// logging setup
app.use(morgan('dev'));

// body Parser setup
app.use(bodyParser.urlencoded(
    {
        extended: true
    })
);
app.use(bodyParser.json());

// cors handling
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
    if (req.method === 'OPTIONS') {
        res.header('Access-Control-Allow-Methods', 'PUT, POST, PATCH, DELETE, GET');
        res.status(200).json({});
    }
    next();
});

// routes
app.use('/api/users', userRoute);
app.use('/login', authenticationRoute);
app.use('/api/tasks', taskRoute);

// error handling
app.use((req, res, next) => {
    const error = new Error('Not Found');
    error.status = 404;
    next(error);
})

// error
app.use((error, req, res) => {
    res.status(error.status || 500);
    res.json({
        message: error.message
    });
});


module.exports = app;