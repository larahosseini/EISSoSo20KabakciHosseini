const mongoose = require('mongoose');

const taskSchema = mongoose.Schema({
    _id: mongoose.Schema.Types.ObjectId,
    created_at: {
        type: Date,
        default: Date.now,
        required: true
    },
    creator: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    helper: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    help_accepted: {
        type: Boolean,
        default: false,
        required: true
    },
    title: {
        type: String,
        enum: [
            'Lebensmittel einkaufen',
            'Medikamente aus der Apotheke abholen',
            'Begleitung für Arztbesuch'
        ],
        required: true,
        trim: true
    },
    description: {
        type: String,
        default: '',
        required: true
    },
    finished: {
        type: Boolean,
        default: false,
        required: true
    }
});

module.exports = mongoose.model('Task', taskSchema);