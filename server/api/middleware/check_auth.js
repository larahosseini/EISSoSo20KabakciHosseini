const jwt = require('jsonwebtoken');
// prüft ob der user eingeloggt ist
module.exports = (req, res, next) => {
    try {
        // jwt token aus dem header ziehen: token sieht so aus : "Bearer token", deshalb splitten
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