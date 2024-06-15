import React, { useState } from 'react';
import './Chatbox.css';
import InputForm from '../Common/InputForm';
import { FaFacebookMessenger } from "react-icons/fa";

const Chatbox = () => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleChatbox = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div className={`chatbox-container ${isOpen ? 'open' : ''}`}>
      <div className={`chatbox-header ${isOpen ? 'open' : ''}`} onClick={toggleChatbox}>
        <button className="chatbox-toggle">
          {isOpen ? '−' : <FaFacebookMessenger size={29} />}
        </button>
        {isOpen ? <span>Chat</span> : <></>}
      </div>
      {isOpen && (
        <div className="chatbox-body">
          <div className="chatbox-messages">
            {/* Messages can go here */}
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
            <p>Welcome to the chat!</p>
          </div>
          <InputForm placeholder={"Nhập tin nhắn..."} />
        </div>
      )}
    </div>
  );
}

export default Chatbox;
