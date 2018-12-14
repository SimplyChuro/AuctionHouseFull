import Route from '@ember/routing/route';

export default Route.extend({
  actions: {
    willTransition: function() {
      this.controllerFor('new-password').send('clearFields');
    }
  }
});
