import toast, { ToastBar, Toaster } from "react-hot-toast";
import { IoClose } from "react-icons/io5";

export const ShowSuccessToast = (title, message) => {
    toast.success((t) => (
        <Toast message={message} title={title} t={t} />
    ))
}

export const ShowErrorToast = (title, message) => {
    toast.error((t) => (
        <Toast message={message} title={title} t={t} />
    ))
}

export const Toast = ({ title, message, t }) => {
    return (
        <span style={{ lineHeight: "28px" }} className="d-flex flex-row">
            <span className="d-flex flex-column">
                {title &&
                    <strong>
                        {title}
                    </strong>
                }
                {message &&
                    <span>
                        {message}
                    </span>
                }
            </span>
            <span role="button" className="align-self-center" onClick={() => toast.dismiss(t.id)}>
                <IoClose size={25} />
            </span>
        </span>
    );
}

export default function DismissableToast() {
    return (
        <div>
            <Toaster
                reverseOrder={false}
                position='top-right'
                toastOptions={{
                    style: {
                        borderRadius: '12px',
                        background: '#333',
                        color: '#fff',
                        fontSize: '18px',
                        lineHeight: '32px'
                    },
                    iconTheme: {
                        fontSize: '24px'
                    }
                }}
            >
                {(t) => (
                    <ToastBar toast={t}>
                        {({ icon, message }) => (
                            <>
                                {icon}
                                {message}
                                {t.type !== 'loading' && (
                                    <span role="button" className="align-self-center" onClick={() => toast.dismiss(t.id)}>
                                        <IoClose size={25} />
                                    </span>
                                )}
                            </>
                        )}
                    </ToastBar>
                )}
            </Toaster>
        </div>
    );
}
