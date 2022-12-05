import BoardClient from "./BoardClient";
import ArticleClient from "./ArticleClient";
import HouseClient from "./HouseClient";
import NewsClient from "./NewsClient";
import MemberClient from "./MemberClient";
import CommentClient from "./CommentClient";

// const serverUrl = "http://192.168.204.113:9999";
const serverUrl = "http://localhost:9999";
export const TOKEN_KEY = "ACCESS_TOKEN";

export const boardClient = new BoardClient(serverUrl);
export const articleClient = new ArticleClient(serverUrl);
export const houseClient = new HouseClient(serverUrl);
export const newsClient = new NewsClient(serverUrl);
export const memberClient = new MemberClient(serverUrl);
export const commentClient = new CommentClient(serverUrl);