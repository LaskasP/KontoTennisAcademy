import { defineStore } from "pinia";

export const useCourtsStore = defineStore("courts", {
  state: () => [
    { id: 0, name: "TENNIS01", available: [] },
    { id: 1, name: "TENNIS02", available: [] },
    { id: 2, name: "PADEL01", available: [] }
  ]
});
