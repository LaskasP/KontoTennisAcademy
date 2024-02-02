import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => {
    return {
      username: "",
      firstname: "",
      lastname: "",
      email: "",
      roles: []
    };
  },
  getters: {},
  actions: {
    login() {},
    async signup(payload) {
      const response = await fetch("http://localhost:8081/auth/registration", {
        method: "POST",
        body: JSON.stringify({
          email: payload.email,
          password: payload.password,
          username: payload.username,
          firstname: payload.firstname,
          lastname: payload.lastname
        }),
        headers: { "Content-Type": "application/json" }
      });
      const responseData = await response.json();
      if (response.status !== 201) {
        console.log(responseData);
        throw new Error(responseData.message || "Failed to signup.");
      }
      this.setUser(responseData);
    },
    setUser(responseData) {
      this.firstname = responseData.firstname;
      this.lastname = responseData.lastname;
      this.username = responseData.username;
      this.email = responseData.email;
      this.roles = responseData.roles;
    }
  }
});
