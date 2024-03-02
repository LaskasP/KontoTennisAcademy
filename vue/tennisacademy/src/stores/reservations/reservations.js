// noinspection JSUnresolvedReference

import { defineStore } from "pinia";
import { useAuthStore } from "@/stores/auth/auth.js";

export const useReservationStore = defineStore("reservations", {
  state: () => {
    return { reservations: [] };
  },
  getters: {
    getReservations(state) {
      return state.reservations;
    },
    hasAnyReservation(state) {
      return state.reservations.length > 0;
    }
  },
  actions: {
    async createReservation(payload) {
      if (useAuthStore().isUserLoggedIn) {
        const response = await fetch("http://localhost:8081/reservation", {
          method: "POST",
          body: JSON.stringify({
            ...payload,
            username: useAuthStore().getUsername
          }),
          headers: { "Content-Type": "application/json", Authorization: useAuthStore().getToken }
        });
        const responseData = await response.json();
        if (response.status !== 201) {
          console.log(responseData);
          throw new Error(responseData.messages || "Failed to create reservation.");
        }
        this.reservations.push(responseData);
      }
    }
  },
  persist: true
});
