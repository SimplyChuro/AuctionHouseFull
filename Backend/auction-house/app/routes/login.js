import Route from '@ember/routing/route';

export default Route.extend({
  actions: {
    willTransition: function(transition) {
      this.controllerFor('login').send('clearFields');
    }
  }
});
