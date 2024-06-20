import { Image, Modal } from "react-bootstrap";
import "./Styles.css";

const ProofModal = ({ Show, Close, proof }) => {

  return (
    <>
      <Modal show={Show} onHide={Close} centered className="custom-backdrop">
        <Modal.Header closeButton>
          <Modal.Title>Minh chứng</Modal.Title>
        </Modal.Header>
        <Modal.Body className="text-center">
          {proof && (
            <>
              <Image src={proof} fluid />
            </>
          )}
        </Modal.Body>
      </Modal>
    </>
  );
};

export default ProofModal;
