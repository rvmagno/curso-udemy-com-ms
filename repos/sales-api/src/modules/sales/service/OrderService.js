import OrderRepository from "../repository/OrderRepository.js";
import { sendMessageToProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import { PENDING, ACCEPTED, REJECTED } from "../status/OrderStatus.js";
import { OrderException } from "../exception/OrderException.js"
import { BAD_REQUEST } from "../../../config/constants/httpStatus.js"


class OrderService {

    async createOrder(req){
        try{

            let orderData = req.body
            this.validateOrderDate(orderData);

            const { authUser } = req;
            let order = {
                status: PENDING,
                user: authUser,
                createdAt: new Date(),
                updatedAt: new Date(),
                products: orderData,
            };

            

            await this.validateProductStock(order);

            let createdOrder  = await OrderRepository.save(order)
            sendMessageToProductStockUpdateQueue(createdOrder.products);

            return {
                status : httpStatus.SUCCESS,
                accessToken
            }
        }catch(error){
            console.error(error.message);
            return {
                status : error.status ? error.status : httpStatus.INTERNAL_SERVER_ERROR,
                message: error.status
            }
        }
    }

    async updateOrder(orderMessage){
        try {
            const order = JSON.parse(orderMessage);
            if(order.salesId && !order.status){
                let existingOrder = await OrderRepository.findById(order.salesId);
                if(order.status && order.status !== existingOrder){
                    existingOrder.status = order.status;
                    await OrderRepository.save(existingOrder);
                }
            }
            else{
                console.warn("The order message was not complete");
            }
        } catch (error) {
            console.error('Could not parse order message from queue');
            console.error(error.message);
        }
    }

    validateOrderDate(data){
        if(!data || !data.products){
            throw new OrderException( BAD_REQUEST, 'The products must be informed');
        }
    }

    async validateProductStock(order){
        let stockIsOut = true;
        if(stockIsOut){
            throw new OrderException(
                BAD_REQUEST,
                "The stock is out for the products."
            )
        }

    }

}

export default new OrderService();

