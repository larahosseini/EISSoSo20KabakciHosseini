const mongoose = require('mongoose');


const activationSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    userIdHashed: {type: String, required: true},
    user: {type: mongoose.Schema.Types.ObjectId, ref: 'User'}
});

module.exports = mongoose.model('Activation', activationSchema);