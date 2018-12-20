import { helper } from '@ember/component/helper';
import { isEmpty } from '@ember/utils';

export default Ember.Helper.extend({
  
  compute(params) {
    let products = params[0];
    let sorting = params[1];

    if(isEmpty(sorting) || sorting == 'popularity'){
      return products.sortBy('bids.length').reverse();
    }

    if(sorting == 'rating-high'){
      return products.sortBy('averageScore').reverse();
    }

    if(sorting == 'rating-low'){
      return products.sortBy('averageScore');
    }

    if(sorting == 'newest'){
      return products.sortBy('publishDate').reverse();
    }

    if(sorting == 'oldest'){
      return products.sortBy('expireDate');
    }

    if(sorting == 'price-low'){
      return products.sortBy('startingPrice');
    }

    if(sorting == 'price-high'){
      return products.sortBy('startingPrice').reverse();
    }

    return products;
  }
});
