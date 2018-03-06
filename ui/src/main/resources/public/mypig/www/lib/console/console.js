define(['console-min'], function () {
  window.debug.group("Entering Console module.");
  window.debug.info("console: ", debug);
  window.debug.groupEnd();
  return window.debug;
});
