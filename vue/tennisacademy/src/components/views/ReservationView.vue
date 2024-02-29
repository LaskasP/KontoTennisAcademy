<template>
  <h1>Reservation View KTA</h1>
  <base-button mode="outline" @click="reserve">Reserve Court</base-button>
</template>

<script>
import BaseButton from "@/components/base/BaseButton.vue";
import { useReservationStore } from "@/stores/reservations/reservations.js";

export default {
  name: "ReservationView",
  components: { BaseButton },
  methods: {
    async reserve() {
      let payload = {
        courtEnum: "TENNIS_1",
        reservationDate: new Date(),
        reservationStartTime: "13:30:00",
        reservationEndTime: "15:30:00",
        availableForSecondPlayer: true
      };
      console.log(payload);
      await useReservationStore().createReservation(payload);
    }
  }
};
</script>

<style scoped></style>
