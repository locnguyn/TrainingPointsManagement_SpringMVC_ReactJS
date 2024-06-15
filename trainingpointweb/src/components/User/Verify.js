import { Button, Container, Form } from "react-bootstrap";

const Verify = () => {
  const fields = [
    {
      field: "email",
      type: "email",
      label: "Email",
    },
  ];

  return (
    <Container className="custom-container">
      <h1 className="text-center">ĐĂNG KÝ</h1>
      <Form>
        <Form.Group className="mb-3">
          <Form.Label>Email</Form.Label>
          <Form.Control type="email" placeholder="Email" required></Form.Control>
        </Form.Group>
        <Form.Group className="mb-3">
          <Button type="submit" variant="primary">
            Tiếp Tục
          </Button>
        </Form.Group>
      </Form>
    </Container>
  );
};

export default Verify;