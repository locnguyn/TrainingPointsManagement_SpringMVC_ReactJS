import React, { useContext, useEffect, useRef, useState } from 'react';
import './Chatbox.css';
import InputForm from '../Common/InputForm';
import { Image } from 'react-bootstrap';
import { auth, messageListener, getChatId, getMessages, sendMessage } from '../../configs/firebase';
import { FaMinus } from "react-icons/fa6";
import moment from 'moment';
import { MyUserContext } from '../../configs/Contexts';

moment.locale("vi");

const Chatbox = ({ isOpen, toggleChatbox, chattingUser }) => {
  const [messages, setMessages] = useState([]);
  const chatId = getChatId(chattingUser.email);
  const {user} = useContext(MyUserContext);
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView();
    }
  };

  useEffect(() => {
    if (isOpen) {
      scrollToBottom();
    }
  }, [messages]);

  useEffect(() => {
    const unsubscribe = getMessages(setMessages, chattingUser.email);
    return () => unsubscribe()
    // const listener = messageListener(setMessages, chattingUser.email);
    // return () => listener();
  }, [])

  const send = (message) => {
    sendMessage(chatId, message, user.email, chattingUser.email);
  }

  const getMessageClassName = (messages, index, currentEmail) => {
    const prevMessage = messages[index - 1];
    const nextMessage = messages[index + 1];

    const isSameAsPrevious = prevMessage && prevMessage.email === currentEmail;
    const isSameAsNext = nextMessage && nextMessage.email === currentEmail;

    if (!isSameAsPrevious && !isSameAsNext) {
      return 'message-normal';
    } else if (!isSameAsPrevious && isSameAsNext) {
      return 'message-first';
    } else if (isSameAsPrevious && isSameAsNext) {
      return 'message-middle';
    } else if (isSameAsPrevious && !isSameAsNext) {
      return 'message-last';
    }
  };

  return (
    <div className={`chatbox-container ${isOpen ? 'open' : ''}`}>
      <div className={`chatbox-header ${isOpen ? 'open' : ''} d-flex justify-content-between p-2`}>
        <div className='d-flex flex-row'>
          <div className='avatar me-2'>
            <Image src={chattingUser.avatar ? chattingUser.avatar : "https://i0.wp.com/www.repol.copl.ulaval.ca/wp-content/uploads/2019/01/default-user-icon.jpg?ssl=1"} style={{
              width: "35px",
              height: "35px",
              objectFit: "cover",
            }}
              roundedCircle />
            {chattingUser.status === 'online' && moment(chattingUser.lastActive).fromNow() === "vài giây trước" && (
              <div className="online-indicator-2"></div>
            )}
          </div>
          <div className='d-flex flex-column'>
            {isOpen ? <strong>{chattingUser.lastName + " " + chattingUser.firstName}</strong> : null}
            <small className='status'>{chattingUser.status === "offline" ? "Truy cập " + moment(chattingUser.lastActive).fromNow() : moment(chattingUser.lastActive).fromNow() === "vài giây trước" ? "Đang hoạt động" : "Truy cập " + moment(chattingUser.lastActive).fromNow()}</small>
          </div>
        </div>
        <div className="hide-button" onClick={toggleChatbox}>
          <FaMinus size={20} />
        </div>
      </div>
      {isOpen && (
        <div className="chatbox-body">
          <div className="chatbox-messages">
            {/* Placeholder for messages */}
            {messages && messages.length > 0 && messages.map((message, index) => {
              const messageClass = getMessageClassName(messages, index, message.email);

              return (<div
                key={index}
                className={`chat-message ${message.email === user.email ? 'chat-message-right' : 'chat-message-left'}`}
              >
                {message.email !== user.email && (
                  <div className='avatar-chatbox-body me-2'>
                    <Image src={chattingUser.avatar ? chattingUser.avatar : "https://i0.wp.com/www.repol.copl.ulaval.ca/wp-content/uploads/2019/01/default-user-icon.jpg?ssl=1"} style={{
                      width: "30px",
                      height: "30px",
                      objectFit: "cover",
                    }} roundedCircle />
                  </div>
                )}
                <div className={`message-content ${messageClass}`}>
                  {message.message}
                </div>
              </div>)
            })}
            <div ref={messagesEndRef} />
          </div>
          <div className='chatbox-input'>
            <InputForm placeholder={"Nhập tin nhắn..."} handleSubmit={send} />
          </div>
        </div>
      )}
    </div>
  );
};

export default Chatbox;
