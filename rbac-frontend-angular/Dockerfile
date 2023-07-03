FROM node:16-alpine as node
WORKDIR /app
COPY . .
RUN npm install --force
RUN npm run build

FROM nginx:alpine
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=node /app/dist/meu-projeto /usr/share/nginx/html
