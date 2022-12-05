import axios from 'axios';

export default class BoardClient {
  constructor(baseUrl) {
    this.baseUrl = baseUrl + "/board";
  }

  /**
   * 게시판 목록
   * @return {Promise<{id: number, name: string}[]>}
   */
  async getBoards() {
    const res = await axios.get(this.baseUrl);
    if (res.status === 200) {
      return res?.data;
    }
    return [];
  }

  /**
   * 게시판 생성
   * @return {boolean} 성공 : true, 실패 : false
   */
  async create(name) {
    const res = await axios.post(this.baseUrl, {
      name,
    });
    if (res.status === 200) {
      return true;
    }
    return false;
  }
}