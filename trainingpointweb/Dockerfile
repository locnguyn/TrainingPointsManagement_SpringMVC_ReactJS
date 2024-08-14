# Stage 1
FROM node:22-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# Stage 2
FROM nginx:alpine
# delete default config
# RUN rm -rf /usr/share/nginx/html/*
# copy reactjs build to nginx
COPY --from=build /app/build /usr/share/nginx/html
# COPY /app/nginx/nginx.conf /etc/nginx/nginx.conf

EXPOSE 5679

CMD ["/usr/sbin/nginx", "-g", "daemon off;"]
#CMD ["npm", "start"]