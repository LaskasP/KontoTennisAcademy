import { defineStore } from "pinia";
import { useAuthStore } from "@/stores/auth/auth.js";

export const useReservationStore = defineStore("reservations", {
  state: () => {
    return { reservations: [] };
  },
  getters: {
    getReservations(state) {
      return state.reservations;
    }
  },
  actions: {
    async createReservation(payload) {
      console.log(useAuthStore().isUserLoggedIn);
      if (useAuthStore().isUserLoggedIn) {
        const response = await this.postAuth("http://localhost:8081/reservation", payload);
        const responseData = await response.json();
        if (response.status !== 201) {
          console.log(responseData);
          throw new Error(responseData.messages || "Failed to create reservation.");
        }
        this.reservations.push(responseData);
      }
    },
    async postAuth(url, payload) {
      return await fetch(url, {
        method: "POST",
        body: JSON.stringify({
          ...payload,
          username: useAuthStore().getUsername
        }),
        headers: { "Content-Type": "application/json", Authorization: useAuthStore().getToken }
      });
    }
  },
  persist: true
});
