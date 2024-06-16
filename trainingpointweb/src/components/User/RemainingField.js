import { useContext } from "react";
import { RegisterContext } from "../../configs/Contexts";
import { Button, Form } from "react-bootstrap";

const RemainingField = () => {
    const {Change, faculties, classes, avatar, register, fields, user} = useContext(RegisterContext);
    return (
    <Form onSubmit={register}>
        {fields.map((f) => (
            <Form.Group key={f.field} className="mb-3" controlId={f.field}>
                <Form.Label>{f.label}</Form.Label>
                <Form.Control
                    onChange={(e) => Change(e, f.field)}
                    value={user[f.field] || ""}
                    type={f.type}
                    placeholder={f.label}
                    readOnly={f.field === 'email' || f.field === 'studentCode'}
                />
            </Form.Group>
        ))}
        {classes.length > 0 && (
            <Form.Group className="mb-3">
                <Form.Label>Lớp</Form.Label>
                <Form.Select
                    defaultValue=""
                    required
                    onChange={(e) => Change(e, 'class')}
                >
                    <option value="" disabled hidden>
                        Lớp
                    </option>
                    {classes.map((c) => (
                        <option key={c.id} value={c.id}>
                            {c.name}
                        </option>
                    ))}
                </Form.Select>
            </Form.Group>
        )}
        {faculties.length > 0 && (
            <Form.Group className="mb-3">
                <Form.Label>Khoa</Form.Label>
                <Form.Select
                    defaultValue=""
                    required
                    onChange={(e) => Change(e, 'faculty')}
                >
                    <option value="" disabled hidden>
                        Khoa
                    </option>
                    {faculties.map((f) => (
                        <option key={f.id} value={f.id}>
                            {f.name}
                        </option>
                    ))}
                </Form.Select>
            </Form.Group>
        )}

        <Form.Group className="mb-3" controlId="avatar">
            <Form.Control type="file" accept=".jpg,.png" ref={avatar} />
        </Form.Group>
        <Form.Group className="mb-3">
            <Button type="submit" value="primary">
                Đăng Ký
            </Button>
        </Form.Group>
    </Form>
);
}
export default RemainingField;
