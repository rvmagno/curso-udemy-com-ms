import  Sequelize  from "sequelize";

const sequelize = new Sequelize("auth_db", "admin", "admin",{
    host: "localhost",
    dialect: "postgres",
    quoteIdentifiers: false,
    define: {
        syncOnAssociation: true,
        timestamps: false,
        underscored: true,
        underscoredAll: true,
        freezeTableName: true,
    },
});

sequelize
.authenticate()
.then(()=> {
    console.log("Connection with auth-db has been stablished");
})
.catch((err)=>{
    console.error(`Unable to connect to db ${err.message}`);
})

export default sequelize;