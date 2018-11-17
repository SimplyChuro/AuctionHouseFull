import Route from '@ember/routing/route';

export default Route.extend({
  model(){
    return Ember.RSVP.hash({
      bidList: this.store.findAll('bid')
    })
  }
});
