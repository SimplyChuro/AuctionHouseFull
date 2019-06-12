import { helper } from '@ember/component/helper';

export function getImageId(params) {  
  return "preview-image-" + params;
}

export default helper(getImageId);
