import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model(){
    return hash({
      categoryList: this.store.findAll('category'),
      productListFeatured: this.store.query('product', { name: '', category: 0, featured: 'fav', status: '', rating: '' }),
      productListNew: this.store.query('product', { name: '', category: 0, featured: '', status: 'new', rating: '' }),
      productListEnding: this.store.query('product', { name: '', category: 0, featured: '', status: 'ending', rating: '' }),
      productRandom: this.store.query('product', { name: '', category: 0, featured: 'fav', status: '', rating: '' }).then((list) => {
        const rand = Math.floor(Math.random() * list.get('length'));
        return list.objectAt(rand)
      })
    })
  }
});
