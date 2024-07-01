import React, { useRef, useState, memo } from "react";
import { Button, Form, Modal, Image } from "react-bootstrap";

const MyModal = ({
  Show,
  Close,
  fileChange,
  previewURL,
  CreateReport,
  proof,
  acPartTypedId,
}) => {
  return (
    <>
      <Modal show={Show} onHide={Close}>
        <Modal.Header closeButton>
          <Modal.Title>Báo thiếu</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={(e) => CreateReport(e, acPartTypedId, proof)}>
            <Form.Group className="mb-3" controlId="formReport">
              <Form.Label>Minh Chứng</Form.Label>
              <Form.Control
                type="file"
                accept=".png,.jpeg,.jpg"
                onChange={fileChange}
                ref={proof}
                required
              />
              {previewURL && (
                <div className="mt-3">
                  <Image src={previewURL} fluid />
                </div>
              )}
            </Form.Group>
            <Button variant="secondary" onClick={Close} className="me-2">
              Đóng
            </Button>
            <Button variant="success" type="submit" className="me-2">
              Báo Thiếu
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default MyModal;
