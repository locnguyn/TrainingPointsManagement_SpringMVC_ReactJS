import { AiOutlineReload } from "react-icons/ai";
const LoadMore = ({objName = "", handleLoadMore=()=>{}}) => {

    return (
        <div className="d-flex justify-content-center align-items-center " style={{cursor: "pointer"}} onClick={handleLoadMore}>
            <span className="me-2">
                Hiển thị thêm {objName}
            </span>
            <AiOutlineReload fontWeight={"bold"} />
        </div>
    );
}

export default LoadMore;
