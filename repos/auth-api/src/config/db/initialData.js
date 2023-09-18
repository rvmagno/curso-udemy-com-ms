import bcrypt from "bcrypt";
import User from "../../modules/user/model/User.js";

export async function createInitialData(){
    await User.sync({force: true});
    let password = await bcrypt.hash("123", 10);
    
    await User.create({
        name: 'Test User',
        email: 'testuser@mail.com',
        password: password
    });
    
    await User.create({
        name: 'Test User2',
        email: 'testuser2@mail.com',
        password: password
    });
}