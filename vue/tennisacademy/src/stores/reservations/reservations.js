import { defineStore } from "pinia";
import { useAuthStore } from "@/stores/auth/auth.js";

export const useReservationStore = defineStore("reservations", {
  state: () => {
    return {
      reservations: []
    };
  },
  getters: {
    getReservations(state) {
      return state.reservations;
    }
  },
  actions: {
    async createReservation(payload) {
      if (useAuthStore().isUserLoggedIn()) {
        const response = await this.postAuth("http://localhost:8081/reservation", payload);
        const responseData = await response.json();
        if (response.status !== 201) {
          throw new Error(responseData.messages[0] || "Failed to create reservation.");
        }
        this.reservations.push(responseData);
      }
    },
    async postAuth(url, payload) {
      return await fetch(url, {
        method: "POST",
        body: JSON.stringify({
          ...payload,
          username: useAuthStore().getUsername()
        }),
        headers: { "Content-Type": "application/json", Authorization: useAuthStore().getToken() }
      });
    }
  }
});
