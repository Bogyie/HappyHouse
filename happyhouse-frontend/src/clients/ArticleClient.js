import axios from 'axios';

export default class ArticleClient {

  constructor(serverUrl) {
    this.serverUrl = serverUrl;
    this.baseUrl = serverUrl + "/article";
  }

  /**
   * 글 작성
   * @param {string} title
   * @param {string} content
   * @param {number} authorId
   * @param {number} boardId
   * @param {boolean} board
   * @return {Promise<{articleId: number}>}
   */
  async write(title, content, authorId, boardId, secret) {
    const response = await axios.post(this.baseUrl, {
      title, content: JSON.stringify(content), authorId, boardId, secret
    });
    return response?.data;
  }

  /**
   * 글 목록 반환
   * @param {number} page
   * @param {number} size
   * @return {Promise<{
   *   title: string,
   *   author: string,
   *   views: number,
   *   createdDate: Date
   * }[]>}
   */
  async list(boardId, page, size) {
    const url = `${this.serverUrl}/board/${boardId}/articles`;
    const params = {
      page: page - 1, size
    };
    const { data } = await axios.get(url, { params });
    return data.map(item => ({
      ...item,
      createdDate: (new Date(item.createdDate)).toLocaleDateString("ko-KR")
    }));
  }

  /**
   * 글 정보 반환
   * @param {number} articleId
   */
  async read(articleId) {
    const url = `${this.baseUrl}/${articleId}`;
    return await axios.get(url);
  }

  /**
   * 글 정보 업데이트
   */
  async update(articleId, title, content) {
    const url = `${this.baseUrl}/${articleId}`;
    const jsonContent = JSON.stringify(content);
    return await axios.put(url, { title, content: jsonContent });
  }

  /**
   * 글 삭제
   * @param {number} articleId
   */
  async delete(articleId) {
    const url = `${this.baseUrl}/${articleId}`;
    const response = await axios.delete(url);
    if (response.status === 200) {
      return true;
    }
    return false;
  }

  /**
   * 댓글 불러오기
   */
  async comments(articleId) {
    const url = `${this.baseUrl}/${articleId}/comments`;
    const {data} = await axios.get(url);
    return data.map(item => ({
      ...item,
      createdDate: (new Date(item.createDate)).toLocaleDateString("ko-KR")
    }));
  }
}