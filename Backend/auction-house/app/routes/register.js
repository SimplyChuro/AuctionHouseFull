import Route from '@ember/routing/route';

export default Route.extend({
  model(){
    return Ember.RSVP.hash({
      user: this.store.createRecord('user', { reload: true })
    })
  },
  actions: {
    willTransition: function(transition) {
      this.controllerFor('register').send('clearFields');
    }
  }
});
