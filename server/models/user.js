const mongoose = require('mongoose');

const userSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    username: {type: String, required: true, lowercase: true},
    address: {
        city: {type: String, required: true},
        street: {type: String, required: true},
        street_number: {type: Number, required: true},
        zipcode: {type: Number, required: true}
    }
});

module.exports = mongoose.model('User', userSchema);