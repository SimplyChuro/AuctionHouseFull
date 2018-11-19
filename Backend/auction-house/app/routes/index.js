import Route from '@ember/routing/route';

export default Route.extend({
  beforeModel() {
    this.refresh();
    this.replaceWith('home');
  }
});
