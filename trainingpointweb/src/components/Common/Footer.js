import { Fragment } from "react";
import { Alert, Container } from "react-bootstrap";
import './Styles.css'

const Footer = () => {
    return (
        <Container className="Footer">
            <Alert variant="success" >Điểm Rèn Luyện &copy; 2024</Alert>
            
        </Container>
    );
}

export default Footer;