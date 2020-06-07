const mongoose = require('mongoose');

const userSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    email: {
        type: String,
        required: true,
        lowercase: true,
        unique: true,
        trim: true,
        match: /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/
    },
    password: {type: String, required: true},
    verified: {type: Boolean},
    address: {
        city: {type: String, required: true},
        street: {type: String, required: true},
        street_number: {type: String, required: true},
        zipcode: {type: Number, required: true}
    }
});

module.exports = mongoose.model('User', userSchema);