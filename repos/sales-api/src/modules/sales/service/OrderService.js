import OrderRepository from "../repository/OrderRepository.js";
import { sendMessageToProductStockUpdateQueue } from "../../product/rabbitmq/productStockUpdateSender.js";
import { PENDING, ACCEPTED, REJECTED } from "../status/OrderStatus.js";
import { OrderException } from "../exception/OrderException.js" ;
import { BAD_REQUEST } from "../../../config/constants/httpStatus.js" ;
import ProductClient  from "../../product/client/ProductClient.js";


class OrderService {

    async createOrder(req){
        try{

            let orderData = req.body
            this.validateOrderDate(orderData);

            const { authUser } = req;
            const { authorization } = req.headers;
            let order = createInitialOrderData(orderData, authUser);

            await this.validateProductStock(order, authorization);

            let createdOrder  = await OrderRepository.save(order)
            this.sendMessage(createdOrder);

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

    createInitialOrderData(orderData, authUser){
        return {
            status: PENDING,
            user: authUser,
            createdAt: new Date(),
            updatedAt: new Date(),
            products: orderData.products,
        };
    }

    async updateOrder(orderMessage){
        try {
            const order = JSON.parse(orderMessage);
            if(order.salesId && !order.status){
                let existingOrder = await OrderRepository.findById(order.salesId);
                if(order.status && order.status !== existingOrder){
                    existingOrder.status = order.status;
                    existingOrder.updatedAt = new Date();
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

    async validateProductStock(order, token){
        let stockIsOut = await ProductClient.checkProducStock(order.products, token);
        ProductClient
        if(stockIsOut){
            throw new OrderException(
                BAD_REQUEST,
                "The stock is out for the products."
            )
        }

    }
    sendMessage(createdOrder){
        const message ={
            salesId: createdOrder.id,
            products: createdOrder.products,
        }
        sendMessageToProductStockUpdateQueue(message);
    }

    async findById(req) {
        try {
          const { id } = req.params;
          const { transactionid, serviceid } = req.headers;
          console.info(
            `Request to GET order by ID ${id} | [transactionID: ${transactionid} | serviceID: ${serviceid}]`
          );
          this.validateInformedId(id);
          const existingOrder = await OrderRepository.findById(id);
          if (!existingOrder) {
            throw new OrderException(BAD_REQUEST, "The order was not found.");
          }
          let response = {
            status: SUCCESS,
            existingOrder,
          };
          console.info(
            `Response to GET order by ID ${id}: ${JSON.stringify(
              response
            )} | [transactionID: ${transactionid} | serviceID: ${serviceid}]`
          );
          return response;
        } catch (err) {
          return {
            status: err.status ? err.status : INTERNAL_SERVER_ERROR,
            message: err.message,
          };
        }
    }

    validateInformedId(id) {
        if (!id) {
          throw new OrderException(BAD_REQUEST, "The order ID must be informed.");
        }
      }

}

export default new OrderService();

