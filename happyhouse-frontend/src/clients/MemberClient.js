import axios from 'axios';
import { TOKEN_KEY } from "./index";

export default class MemberClient {
  constructor(serverUrl) {
    this.serverUrl = serverUrl;
    this.baseUrl = serverUrl + '/member';
  }

  /**
   * 로그인
   * @param {string} username
   * @param {string} password
   * @return {Promise<{}>}
   */
  async login(username, password) {
    const url = this.baseUrl + '/login';
    try {
      const response = await axios.post(url, {
        username, password
      });
      if (response.status === 200 && response?.data?.accessToken) {
        localStorage.setItem(TOKEN_KEY, response.data.accessToken);
        return true;
      }
    } catch {
      console.log("로그인 실패");
    }
    return false;
  }

  /**
   * 회원가입
   * @param {string} username
   * @param {string} password
   * @param {string} nickname
   * @param {string} email
   * @param {string} address
   */
  async register(username, password, nickname, email, address) {
    const response = await axios.post(this.baseUrl, {
      username, password, nickname, email, address
    });
    return response?.data;
  }

  /**
   * 회원수정
   * @param {number} userId
   * @param {string} nickname
   * @param {string} email
   * @param {string} address
   */
  async userModify(userId, nickname, email, address) {
    const url = this.baseUrl + `/${userId}`;
    const accessToken = localStorage.getItem(TOKEN_KEY);
    const newUserInfo = { nickname, email, address };
    const headers = {
      Authorization: accessToken
    };
    if (accessToken) {
      const response = await axios.put(url, newUserInfo, { headers });
      return response?.data;
    }
    return {};
  }

  /**
   * 회원상세정보
   * @param {number} userId
   */
  async userDetail(userId) {
    const url = this.baseUrl + `/${userId}`;
    const accessToken = localStorage.getItem(TOKEN_KEY);

    if (accessToken) {
      const response = await axios.get(url, {
        headers: { Authorization: accessToken },
      });
      return response?.data;
    }
    return {};
  }

  /**
   * 회원탈퇴
   * @param {number} userId
   */
  async userRemove(userId) {
    const url = this.baseUrl + `/${userId}`;
    const accessToken = localStorage.getItem(TOKEN_KEY);

    if (accessToken) {
      const response = await axios.delete(url, {
        headers: { Authorization: accessToken },
      });
      if (response.status === 200) {
        return true;
      }
    }
    return false;
  }

  /**
   * 비밀번호변경
   * @param {string} username
   * @param {string} email
   * @param {string} password
   */
  async findPwd(username, email, password) {
    const url = this.baseUrl + '/password';
    const newInfo = { username, email, password };
    const response = await axios.put(url, newInfo);
    return response?.data;
  }

  /**
   * 작성 게시글 검색
   */
  async findArticle(userid) {
    const url = this.baseUrl + `/${userid}/` + 'articles';
    const accessToken = localStorage.getItem(TOKEN_KEY);
    const response = await axios.get(url, {
      headers: { Authorization: accessToken },
    });
    if (response.status === 200 && response?.data) {
      const data = response?.data;
      return data.map(item => ({
        ...item,
        createdDate: (new Date(item.createdDate)).toLocaleDateString("ko-KR")
      }));
    }
    return [];
  }

  /**
   * 작성 댓글 검색
   */
  async findComments(userid) {
    const url = this.baseUrl + `/${userid}/` + 'comments';
    const accessToken = localStorage.getItem(TOKEN_KEY);
    const response = await axios.get(url, {
      headers: { Authorization: accessToken },
    });
    if (response.status === 200 && response?.data) {
      const data = response?.data;
      return data.map(item => ({
        ...item,
        createDate: (new Date(item.createDate)).toLocaleDateString("ko-KR")
      }));
    }
    return [];
  }
} 