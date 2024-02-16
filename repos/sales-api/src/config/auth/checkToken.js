import jwt from 'jsonwebtoken';
import { promisify } from "util";

import AuthException from "./AuthException.js";

import { MONGO_DB_URL } from '../constants/secrets.js'; 
import { UNAUTHORIZED, INTERNAL_SERVER_ERROR } from '../constants/httpStatus.js'; 


const bearer = "bearer ";

export default async (req, res, next) => {

    try {
        
        const { authorization } = req.headers;
        // console.log(req.headers);
        if(!authorization){
            throw new AuthException(
                UNAUTHORIZED,
                "Access token was not informed"
                );
            }
            
        let accessToken = authorization;
        if(accessToken.toLowerCase().includes(bearer)){
            accessToken = accessToken.replace(bearer, "");
        }

        const decoded = await promisify(jwt.verify)(
            accessToken,
            API_SECRET
        );
        req.authUser = decoded.authUser;
        return next();

    } catch (error) {
        const status = error.status ? error.status : INTERNAL_SERVER_ERROR;
        return res.status(status).json({
                status : status,
                message: error.message
            });
    }
}