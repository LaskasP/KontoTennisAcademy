import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => {
    return {
      username: "",
      firstname: "",
      lastname: "",
      email: "",
      roles: [],
      token: null
    };
  },
  getters: {},
  actions: {
    async login(payload) {
      const response = await this.postAuth("http://localhost:8081/auth/login", payload);
      const responseData = await response.json();
      if (response.status !== 200) {
        throw new Error(responseData.messages[0] || "Failed to login.");
      }
      this.setUser(responseData, response.headers.get("Authorization"));
    },
    async signup(payload) {
      const response = await this.postAuth("http://localhost:8081/auth/registration", payload);
      const responseData = await response.json();
      if (response.status !== 201) {
        throw new Error(responseData.messages[0] || "Failed to signup.");
      }
      this.setUser(responseData, response.headers.get("Authorization"));
    },
    setUser(responseData, token) {
      this.firstname = responseData.firstname;
      this.lastname = responseData.lastname;
      this.username = responseData.username;
      this.email = responseData.email;
      this.roles = responseData.roles;
      this.token = token;
    },
    async postAuth(url, payload) {
      return await fetch(url, {
        method: "POST",
        body: JSON.stringify({
          ...payload
        }),
        headers: { "Content-Type": "application/json" }
      });
    }
  }
});
