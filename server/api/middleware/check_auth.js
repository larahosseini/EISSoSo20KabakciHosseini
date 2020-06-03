const jwt = require('jsonwebtoken');
// prüft ob der user eingeloggt hat
module.exports = (req, res, next) => {
    try {
        const token = req.headers.authorization.split(' ')[1];
        req.userData = jwt.verify(token, process.env.JWT_SECRET_KEY);
        next();
    } catch (error) {
        return res.status(401).json(
            {
                message: 'Authentication failed'
            }
        );
    }
};