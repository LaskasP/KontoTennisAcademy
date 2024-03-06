// noinspection JSUnusedGlobalSymbols

import { defineStore, getActivePinia } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => {
    return {
      username: "",
      firstname: "",
      lastname: "",
      email: "",
      roles: [],
      token: null,
      refreshToken: null,
      isLoggedIn: false,
      expiresIn: null,
      expirationDate: null
    };
  },
  getters: {
    getToken(state) {
      return state.token;
    },
    getUsername(state) {
      return state.username;
    },
    isUserLoggedIn(state) {
      return state.isLoggedIn;
    }
  },
  actions: {
    async login(payload) {
      const response = await fetch("http://localhost:8081/auth/login", {
        method: "POST",
        body: JSON.stringify({
          ...payload
        }),
        headers: { "Content-Type": "application/json" }
      });
      const responseData = await response.json();
      if (response.status !== 200) {
        throw new Error(responseData.messages[0] || "Failed to login.");
      }
      this.setUser(responseData, response.headers);
    },
    async signup(payload) {
      const response = await fetch("http://localhost:8081/auth/registration", {
        method: "POST",
        body: JSON.stringify({
          ...payload
        }),
        headers: { "Content-Type": "application/json" }
      });
      const responseData = await response.json();
      if (response.status !== 201) {
        throw new Error(responseData.messages[0] || "Failed to signup.");
      }
      this.setUser(responseData, response.headers);
    },
    async logout() {
      if (this.isLoggedIn) {
        await fetch("http://localhost:8081/auth/logout" + "?username=" + this.username, {
          method: "GET",
          headers: { Authorization: this.token }
        });
        // if (response.status !== 200) {
        //   throw new Error("Failed to logout.");
        // }
        const pinia = getActivePinia();
        pinia._s.forEach((store) => store.$reset());
      }
    },
    async refreshAccessToken() {
      const response = await fetch("http://localhost:8081/auth/refreshment", {
        method: "GET",
        headers: { "Authorization-Refresh": this.refreshToken }
      });
      const responseData = await response.json();
      if (!response.ok) {
        throw new Error(responseData.messages[0] || "Failed to refresh.");
      }
      this.token = response.headers.get("Authorization");
      this.refreshToken = response.headers.get("Authorization-Refresh");
    },
    setUser(responseData, headers) {
      this.firstname = responseData.firstname;
      this.lastname = responseData.lastname;
      this.username = responseData.username;
      this.email = responseData.email;
      this.roles = responseData.roles;
      this.isLoggedIn = true;
      this.token = headers.get("Authorization");
      this.refreshToken = headers.get("Authorization-Refresh");
      this.expirationDate = new Date(+parseJwtExpiration(this.token) * 1000);
      this.expiresIn = this.expirationDate.getTime() - new Date().getTime() - 3800;
      setTimeout(async function () {
        await this.refreshAccessToken();
      }, this.expiresIn);
    }
  },
  persist: true
});

function parseJwtExpiration(token) {
  var base64Url = token.split(".")[1];
  var base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
  var jsonPayload = decodeURIComponent(
    window
      .atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );
  return JSON.parse(jsonPayload).exp;
}
