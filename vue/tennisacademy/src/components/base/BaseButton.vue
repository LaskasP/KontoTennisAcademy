<!--suppress ALL -->
<script>
export default {
  name: "BaseButton",
  props: {
    link: {
      type: Boolean,
      required: false,
      default: false,
      description: "Pass this prop if it is a router link"
    },
    to: {
      type: String,
      required: false,
      default: "/"
    },
    mode: {
      type: String,
      required: false,
      default: null
    }
  }
};
</script>

<template>
  <button v-if="!link" :class="mode">
    <slot></slot>
  </button>
  <router-link v-else :class="mode" :to="to">
    <slot></slot>
  </router-link>
</template>

<style scoped>
button,
a {
  color: #f391e3;
  border: 1px solid transparent;
  text-decoration: none;
  padding: 0.75rem 1.5rem;
  font: inherit;
  background-color: #270041;
  color: white;
  cursor: pointer;
  margin-right: 0.5rem;
  display: inline-block;
}

a:hover,
a:active,
button:hover,
button:active {
  background-color: rgba(58, 0, 97, 0.11);
  color: black;
}

.outline {
  background-color: transparent;
  text-decoration: none;
  color: #f391e3;
  display: inline-block;
  padding: 0.75rem 1.5rem;
  border: 1px solid transparent;
}

.outline:hover,
.outline:active {
  border-color: #f391e3;
  background-color: transparent;
  color: white;
}
</style>
