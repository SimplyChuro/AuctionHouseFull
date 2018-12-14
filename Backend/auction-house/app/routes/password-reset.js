import Route from '@ember/routing/route';

export default Route.extend({
  actions: {
    willTransition: function() {
      this.controllerFor('password-reset').send('clearFields');
    }
  }  
});
