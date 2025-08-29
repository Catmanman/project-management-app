# ---------- Build stage ----------
FROM node:20-bullseye AS build
WORKDIR /app

# Install deps (lockfile strict; matches your local npm@10)
COPY package*.json ./
RUN npm install -g npm@10 \
 && npm ci --no-audit --no-fund --legacy-peer-deps

# Build Angular
COPY . .
RUN npm run build -- --configuration production

# ---------- Runtime stage ----------
FROM node:20-bullseye
WORKDIR /app
ENV NODE_ENV=production

# Copy built assets
COPY --from=build /app/dist ./dist

# Copy server and install only prod deps (needs "express" in dependencies)
COPY server.js ./
COPY package*.json ./
RUN npm ci --omit=dev --no-audit --no-fund --legacy-peer-deps

EXPOSE 4200
CMD ["node", "server.js"]
