import bcrypt from "bcrypt";
import jwt from "jsonwebtoken"

import UserRepository from "../repository/UserRepository.js";
import UserException from "../exception/UserException.js";
import * as httpStatus from "../../../config/constant/httpStatus.js";
import * as secret from "../../../config/constant/secrets.js";



class UserService{

    async getAccessToken(req){
        try {
            const { email, password } = req.body;
            this.validateAccessTokenData(email, password);
            let user = await UserRepository.findByEmail(email);
            const authUser = { id: user.id, name: user.name, email: user.email}
            this.validateUserNotFound(user);
            this.validateAuthenticatedUser(user, authUser);
            
            console.log(user);
            await this.validatePassword(password, user.password);

            const accessToken = jwt.sign( {authUser},  secret.API_SECRET , {expiresIn: "1d"}  )
            return {
                status : httpStatus.SUCCESS,
                accessToken
            }

        } catch (error) {
            console.error(error.message);
            return {
                status : error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.status
            }
        }
    }

    async findByEmail(req){
        try {
            const {email } = req.params;
            this.validateRequestData(email);
            let user = await UserRepository.findByEmail(email);
            this.validateUserNotFound(user);

            return {
                status : httpStatus.SUCCESS,
                user: {
                    id: user.id,
                    name: user.name,
                    email: user.email
                }
            }
        } catch (error) {
            console.error(error.message);
            return {
                status : error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.status
            }
        }
    }

    validateRequestData(email){
        if(!email){
            throw new UserException(
                httpStatus.BAD_REQUEST,
                "User email was not informed"
                );
        }
    }

    validateUserNotFound(user){
        if(!user){
            throw new UserException(
                httpStatus.BAD_REQUEST,
                "User email was not found"
                );
        }
    }

    validateAccessTokenData(email, password){
        if(!email || !password){
            throw new UserException(httpStatus.UNAUTHORIZED, "email or password wrong");
        }
    }

    async validatePassword(password, hashPassword){
        if(!await bcrypt.compare(password, hashPassword)){
            throw new UserException(httpStatus.UNAUTHORIZED, "Password doesn't match.");
        }
    }

    validateAuthenticatedUser(user, authUser){
        console.log("user" + user);
        console.log(authUser);
        if(!authUser || user.id !== authUser.id ){
            throw new UserException(
                httpStatus.FORBIDDEN,
                "You cannot see this user data");
        }
    }
    
}

export default new UserService();