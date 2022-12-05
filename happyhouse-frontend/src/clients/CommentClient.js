import axios from 'axios';
import { TOKEN_KEY } from "./index";
import { getUserIdFromToken } from "@/tools/JwtTools";

export default class MemberClient {
  constructor(serverUrl) {
    this.serverUrl = serverUrl;
    this.baseUrl = serverUrl + '/comment';
  }

  /**
   * 댓글 삭제
   * @param {number} commentId
   */
  async delete(commentId) {
    const url = `${this.baseUrl}/${commentId}`;
    const response = await axios.delete(url);
    if (response.status === 200) {
      return true;
    }
    return false;
  }

  /**
   * 댓글 작성
   * @param {string} content
   * @param {number} authorId
   * @param {number} articleId
   * @param {boolean} secret
   */
  async create(comment, articleId, secret) {
    const accessToken = localStorage.getItem(TOKEN_KEY);
    const authorId = getUserIdFromToken(accessToken);
    if (accessToken) {
      const response = await axios.post(this.baseUrl, { content: comment, authorId, articleId, secret })
      console.log(response);
      return response?.data;
    }
    return false;
  }
}
