const nodeMailer = require('nodemailer');

const transporter = nodeMailer.createTransport(
    {
        service: 'gmail',
        host: 'smtp.gmail.com',
        secure: false,
        auth: {
            user: process.env.EMAIL,
            pass: process.env.EMAIL_PW
        }
    }
);

exports.sendRegistrationEmail = function sendRegistrationEmail(email, activationLink) {
    transporter.sendMail({
        from: 'Support <process.env.EMAIL>',
        to: email,
        subject: 'Helper - Activation of your Account',
        text: 'Please follow the link to activate your account:\n' + activationLink
    }, function (error, result) {
        if (error) {
            console.log(error);
        } else {
            console.log('Email sent');
        }
    });
}