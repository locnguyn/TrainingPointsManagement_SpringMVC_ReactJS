import { PayPalButtons, PayPalScriptProvider } from "@paypal/react-paypal-js";
import { Button, Image, Modal } from "react-bootstrap";
import APIs, { authApi, endpoints, paypal } from "../../configs/APIs";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import "./Styles.css";
import ZaloPayLogo from "./ZaloPay_Logo.png";

const Payment = ({ Show, Close, setPaid }) => {
  const Message = ({ content }) => {
    return <p>{content}</p>;
  };

  const [message, setMessage] = useState("");
  // const [order, setOrder] = useState(null);

  // const [token, setToken] = useState(null);

  const generateAcessToken = async () => {
    try {
      let g = await paypal().post(endpoints["paypal-genrate-access-token"]);
      console.log(g.data.access_token);
      return g.data.access_token;
    } catch (ex) {
      console.error(ex);
    }
    return null;
  };

  const zalopayCreateOrder = async () => {
    try {
      const res = await authApi().post(endpoints["zalopay-create-order"], {"amount": 10000});

      if (res.data) {
        window.location.href = res.data.orderurl;
      }
    } catch (ex) {
      toast.error("Có lỗi xảy ra, vui lòng thử lại sau");
    }
  };

  const payVnPay = async () => {
    const form = new FormData();
    form.append("amount", 10000);
    form.append("orderInfo", "Thanh toán cho Điểm rèn luyện Lộc Hiếu");
    try {
      const res = await authApi().post(endpoints["vn-pay"], form);
      console.log(res);

      if (res.data) {
        window.location.href = res.data;
      }
    } catch (error) {
      toast.error("Có lỗi xảy ra, vui lòng thử lại sau");
    }
  };

  const paypalCreateOrder = async () => {
    const access_token = await generateAcessToken();
    if (access_token != null) {
      // setToken(access_token);
      let param = new URLSearchParams();
      param.set("access_token", access_token);
      param.set("amount", 0.5);
      try {
        let c = await authApi().post(
          `${endpoints["paypal-create-order"]}?${param}`
        );
        console.log(c.data);
        const orderData = await c.data;

        if (orderData.id) {
          // setOrder(orderData.id);
          return orderData.id;
        } else {
          const errorDetail = orderData?.details?.[0];
          const errorMessage = errorDetail
            ? `${errorDetail.issue} ${errorDetail.description} (${orderData.debug_id})`
            : JSON.stringify(orderData);

          throw new Error(errorMessage);
        }
      } catch (ex) {
        console.error(ex);
        setMessage(`Could not initiate PayPal Checkout...${ex}`);
      }
    }
  };

  // const captureOrder = async (token, orderId) => {
  //   if (token != null) {
  //       console.log("capture");
  //     let param = new URLSearchParams();
  //     param.set("access_token", token);
  //     try {
  //       let c = await authApi().post(
  //         endpoints["paypal-capture-order"](orderId)
  //       );

  //       const orderData = await c.data;

  //       const errorDetail = orderData?.details?.[0];

  //       if (errorDetail?.issue === "INSTRUMENT_DECLINED") {
  //         console.log("decline");
  //       } else if (errorDetail) {
  //         throw new Error(`${errorDetail.description} (${orderData.debug_id})`);
  //       } else {
  //         const transaction = orderData.purchase_units[0].payments.captures[0];
  //         setMessage(`Transaction ${transaction.status}: ${transaction.id}`);
  //         console.log(
  //           "Capture result",
  //           orderData,
  //           JSON.stringify(orderData, null, 2)
  //         );
  //         Close();
  //       }
  //     } catch (ex) {
  //       console.error(ex);
  //       setMessage(`Sorry, your transaction could not be processed...${ex}`);
  //     }
  //   }
  // };

  const approve = () => {
    setPaid(true);
    // ExportPDF();
    Close();
  };

  return (
    <>
      <Modal show={Show} onHide={Close} centered>
        <Modal.Header closeButton>
          <Modal.Title>Vui Lòng Thanh Toán</Modal.Title>
        </Modal.Header>
        <Modal.Body className="text-center">
          <Button className="mb-3 button-style" onClick={zalopayCreateOrder}>
            <img src={ZaloPayLogo} alt="ZaloPay Logo" width={120} />
          </Button>
          <Button
            className="mb-3 button-style"
            onClick={payVnPay}
          >
            <div className="d-flex flex-row align-items-center">
              <div className="col">
                <div className="a">
                  Ví điện tử{" "}
                  <span className="vnpay-logo b">
                    <span className="vnpay-red">VN</span>
                    <span className="vnpay-blue">PAY</span>
                  </span>
                </div>
              </div>
              <div className="col-auto">
                <div className="icon">
                  <img src="./vnPay.svg" alt="" />
                </div>
              </div>
            </div>
          </Button>
          <PayPalScriptProvider
            options={{ clientId: "sb", currency: "USD", intent: "capture" }}
          >
            <PayPalButtons
              style={{
                shape: "pill",
                layout: "vertical",
                color: "gold",
                label: "paypal",
                captureOrder: false,
              }}
              createOrder={async () => await paypalCreateOrder()}
              onApprove={() => {
                approve();
                console.log("Thanh toán thành công");
              }}
              onCancel={() => Close()}
            ></PayPalButtons>
          </PayPalScriptProvider>
        </Modal.Body>
        <Modal.Footer>
          <Message content={message} />
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default Payment;
